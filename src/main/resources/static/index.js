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

            $scope.newSection = function (opinion) {
                var section = {
                    number: opinion.sections.length + 1,
                    opinionPoll: {id: opinion.id},
                    questions: []
                };
                opinion.sections.push(section);
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
                            //console.log(response);
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
                $scope.opinion = data;
                if (!$scope.opinion.sections || $scope.opinion.sections.length == 0) {
                    alert("Empty opinion poll!");
                    return;
                }
                $scope.currentSection = $scope.opinion.sections[0];

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
                    }
                };


            }


            function errorCallback(resp) {
                alert(resp.status + ": " + resp.statusText);
            }

        }]);