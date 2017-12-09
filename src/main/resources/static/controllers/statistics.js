app.controller('StatisticsController', ['$scope', '$http', '$routeParams',
    function ($scope, $http, $routeParams) {

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
        }


        function errorCallback(response) {
            console.log(response);
            alert('Ошибка при получении данных с сервера.');
        }

    }]);