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
                .when('/opinion/:code', {
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
    .controller('ConstructorController', ['$scope', '$http', '$routeParams', '$location',
        function ($scope, $http, $routeParams, $location) {

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
                var opinion = data;
                opinion.code = code;
                if (!opinion.sections) {
                    opinion.sections = [];
                }
                $scope.opinion = opinion;
                $scope.publicLink = $location.absUrl().split('#')[0] + '#/opinion/' + opinion.publicCode;
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
                    question: {id: question.id}
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


        }]);