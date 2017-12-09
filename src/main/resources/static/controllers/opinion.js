app.controller('OpinionController', ['$scope', '$http', '$routeParams', '$location',
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
            errorCallback
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


        function errorCallback(response) {
            console.log(response);
            alert('Ошибка при получении данных с сервера.');
        }

    }]);