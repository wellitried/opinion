app.controller('ConstructorController', ['$scope', '$http', '$routeParams', '$location', '$interval',
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
            errorCallback
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
                if (respondentInfoSection) {
                    var index = opinion.sections.indexOf(respondentInfoSection);
                    opinion.sections.splice(index, 1);
                    opinion.sections.unshift(respondentInfoSection);
                }
            }
            $scope.opinion = opinion;
            $scope.shareLink = $location.absUrl().split('#')[0] + '#/opinion/' + opinion.publicCode;
            $scope.viewLink = '#/opinion/' + opinion.publicCode + '/test';
            $scope.statisticsLink = '#/new/' + opinion.code + '/statistics';
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

        function errorCallback(response) {
            console.log(response);
            alert('Ошибка при получении данных с сервера.');
        }


    }]);