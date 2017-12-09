app.controller('IndexController', ['$scope', '$http', '$location',
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
                errorCallback
            );
        };

        function errorCallback(response) {
            console.log(response);
            alert('Ошибка при получении данных с сервера.');
        }


    }]);