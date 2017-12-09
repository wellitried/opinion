var app = angular.module('app', ['ngRoute']);
app.config(['$routeProvider', '$locationProvider',
    function ($routeProvider, $locationProvider) {
        $routeProvider
            .when('/', {
                templateUrl: '../templates/hello.html',
                controller: 'IndexController'
            })
            .when('/new/:code', {
                templateUrl: '../templates/constructor.html',
                controller: 'ConstructorController'
            })
            .when('/new/:code/statistics', {
                templateUrl: '../templates/statistics.html',
                controller: 'StatisticsController'
            })
            .when('/opinion/:publicCode/:test?', {
                templateUrl: '../templates/opinion.html',
                controller: 'OpinionController'
            })
            .otherwise({
                redirectTo: '/'
            });
        $locationProvider.hashPrefix("");
    }]);