angular
    .module('App', ['ngRoute'])
    .config(['$routeProvider', '$locationProvider',
        function ($routeProvider, $locationProvider) {
            $routeProvider
                .when('/', {
                    templateUrl: 'templates/hello.html',
                    controller: 'IndexController'
                })
                .when('/new/:code', {
                    templateUrl: 'templates/constructor.html',
                    controller: 'ConstructorController'
                })
                .when('/opinion/:publicCode/:test?', {
                    templateUrl: 'templates/opinion.html',
                    controller: 'OpinionController'
                })
                .otherwise({
                    redirectTo: '/'
                });
            $locationProvider.hashPrefix("");
        }])
    .controller('IndexController', ['$scope', '$http', '$location',
        function ($scope, $http, $location) {

            $scope.toNewOpinionPoll = function () {
                $http({
                    method: 'GET',
                    url: '/getnewcode'
                }).then(
                    function (response) {
                        if (response.data) {
                            $location.path('/new/' + response.data);
                        } else {
                            errorCallback(response);
                        }
                    },
                    function (response) {
                        errorCallback(response);
                    }
                );
            };

            function errorCallback(resp) {
                alert(resp.status + ": " + resp.statusText);
            }


        }])
    .controller('ConstructorController', ['$scope', '$http', '$routeParams', '$location', '$interval',
        function ($scope, $http, $routeParams, $location, $interval) {

            var code = $routeParams.code;

            $http({
                method: 'POST',
                url: '/makeopinionpoll',
                data: angular.toJson({code: code})
            }).then(
                function (response) {
                    if (response.data && response.data.id) {
                        init(response.data);
                    } else {
                        errorCallback(response);
                    }

                },
                function (response) {
                    errorCallback(response);
                }
            );

            function init(data) {
                console.log(data);
                var opinion = data;
                opinion.code = code;
                if (!opinion.sections) {
                    opinion.sections = [];
                } else {
                    var respondentInfoSection = opinion.sections.find(function (section) {
                        return section.type === 'RESPONDENT_INFO';
                    });
                    var index = opinion.sections.indexOf(respondentInfoSection);
                    opinion.sections.splice(index, 1);
                    opinion.sections.unshift(respondentInfoSection);
                }
                $scope.opinion = opinion;
                $scope.shareLink = $location.absUrl().split('#')[0] + '#/opinion/' + opinion.publicCode;
                $scope.viewLink = '#/opinion/' + opinion.publicCode + '/test';
                initAutosave();
            }

            function initAutosave() {
                var stop = $interval(function () {
                    $scope.saveOpinion($scope.opinion);
                }, 60000);

                $scope.stopInterval = function () {
                    $interval.cancel(stop);
                    stop = undefined;
                };

                $scope.$on('$destroy', function () {
                    $scope.stopInterval();
                });
            }

            $scope.removeSection = function (section) {
                if (section.id) {
                    remove('section', section.id)
                }
                var sectionIndex = $scope.opinion.sections.indexOf(section);
                $scope.opinion.sections.splice(sectionIndex, 1);
            };

            $scope.removeAnswer = function (answer, question) {
                if (answer.id) {
                    remove('answer', answer.id)
                }
                var answerIndex = question.answers.indexOf(answer);
                question.answers.splice(answerIndex, 1);
            };

            $scope.removeQuestion = function (question, section) {
                if (question.id) {
                    remove('question', question.id)
                }
                var questionIndex = section.questions.indexOf(question);
                section.questions.splice(questionIndex, 1);
            };

            function remove(type, id) {
                $http({
                    method: 'POST',
                    url: '/remove/' + type + '/' + id
                })
            }

            $scope.newSection = function (opinion) {
                var section = {
                    opinionPoll: {id: opinion.id},
                    type: "QUESTIONS",
                    questions: []
                };
                opinion.sections.push(section);
            };

            $scope.addPersonInfoSection = function (opinion) {
                var section = {
                    opinionPoll: {id: opinion.id},
                    type: "RESPONDENT_INFO",
                    questions: []
                };
                opinion.sections.unshift(section);
            };

            $scope.personInfoSectionExists = function (opinion) {
                if (!opinion) {
                    return;
                }
                return opinion.sections.some(function (section) {
                    return section.type === 'RESPONDENT_INFO';
                });
            };

            $scope.setAnswerTypeText = function (question, isText) {
                question.answers = [];
                if (isText) {
                    $scope.newAnswer(question);
                }
                question.answerIsText = isText;
            };

            $scope.answerIsText = function (question) {
                return question.answerIsText === true;
            };

            $scope.fieldIsDefined = function (field) {
                return typeof field !== 'undefined'
            };

            $scope.multipleAnswersIsDisabled = function (question) {
                return question.answerIsText === true || typeof question.answerIsText === 'undefined';
            };

            $scope.newQuestion = function (section) {
                var question = {
                    section: {id: section.id},
                    answers: []
                };
                section.questions.push(question);
            };

            $scope.newAnswer = function (question) {
                var answer = {
                    question: {id: question.id},
                    navigateToSectionId: null
                };
                question.answers.push(answer);
            };

            $scope.saveOpinion = function (opinion) {

                console.log(opinion);

                $scope.loading = true;
                $http({
                    method: 'POST',
                    url: '/saveopinionpoll',
                    data: angular.toJson(opinion)
                }).then(
                    function (response) {
                        if (response.data && response.data.id) {
                            $scope.loading = false;
                            $scope.opinion = response.data;
                        } else {
                            errorCallback(response);
                        }
                    },
                    function (response) {
                        $scope.loading = false;
                        errorCallback(response);
                    }
                );
            };

            function errorCallback(resp) {
                alert(resp.status + ": " + resp.statusText);
            }


        }])
    .controller('OpinionController', ['$scope', '$http', '$routeParams', '$location',
        function ($scope, $http, $routeParams, $location) {

            var publicCode = $routeParams.publicCode;
            var test = $routeParams.test;

            $http({
                method: 'GET',
                url: '/getopinionpoll/' + publicCode + (test ? '/true' : '/false')
            }).then(
                function (response) {
                    if (response.data && response.data.id) {
                        init(response.data);
                    } else {
                        errorCallback(response);
                    }
                },
                function (response) {
                    errorCallback(response);
                }
            );

            function init(data) {
                console.log(data);
                if (!data.sections || data.sections.length == 0) {
                    alert("Empty opinion poll!");
                    return;
                }
                $scope.opinion = data;
                $scope.currentSection = $scope.opinion.sections[0];
                $scope.finished = false;

                $scope.showPreviousSectionButton = function () {
                    return $scope.opinion.sections.indexOf($scope.currentSection) !== 0;
                };

                $scope.previousSection = function () {
                    var index = $scope.opinion.sections.indexOf($scope.currentSection);
                    if ($scope.opinion.sections[index - 1]) {
                        $scope.currentSection = $scope.opinion.sections[index - 1];
                    }
                };

                $scope.nextSection = function () {
                    var index = $scope.opinion.sections.indexOf($scope.currentSection);
                    if ($scope.opinion.sections[index + 1]) {
                        $scope.currentSection = $scope.opinion.sections[index + 1];
                    } else {
                        $scope.finished = true;
                        if (!test) {
                            $http({
                                method: 'POST',
                                url: '/saveopinionpoll',
                                data: angular.toJson($scope.opinion)
                            })
                        }
                    }
                };


            }


            function errorCallback(resp) {
                alert(resp.status + ": " + resp.statusText);
            }

        }]);