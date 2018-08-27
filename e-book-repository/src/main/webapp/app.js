/**
 * Created by Jasmina on 16/06/2018.
 */
'use strict';

var app = angular.module('app', ['ui.router', 'ngMessages', 'ngMaterial', 'md.data.table', 'hljs']);

app.factory('authInterceptor', ['$q', '$injector', function ($q, $injector) {
    var authInterceptor = {
        responseError: function (response) {
            var $state = $injector.get('$state');
            if ($state.current.name !== 'login' && response.status == 401) {
                $state.transitionTo('login');
            }
            return $q.reject(response);
        }
    };
    return authInterceptor;
}]);

app.config(function ($stateProvider, $locationProvider, $urlRouterProvider, $httpProvider, $mdThemingProvider, $mdDateLocaleProvider, hljsServiceProvider) {

    $mdThemingProvider.theme('default')
        .primaryPalette('indigo')
        .accentPalette('blue');

    $urlRouterProvider.otherwise('/home');

    $httpProvider.interceptors.push('authInterceptor');

    $stateProvider
        .state('login', {
            url: '/login',
            controller: 'LoginController',
            templateUrl: 'pages/login.html'
        })
        .state('navigation', {
            url: '',
            abstract: true,
            controller: 'NavigationController',
            templateUrl: 'pages/navigation.html'
        })
        .state('navigation.home', {
            url: '/home',
            controller: 'HomeController',
            templateUrl: 'pages/home.html'
        })
        .state('navigation.category',{
            url: '/category',
            controller: 'CategoryController',
            templateUrl: 'pages/category.html'
        })
        .state('navigation.ebook',{
            url: '/ebook',
            controller: 'EBookController',
            templateUrl: 'pages/ebook.html'
        })
        .state('navigation.catEBook',{
            url: '/catebook',
            controller: 'EBookController',
            templateUrl: 'pages/ebook.html',
            params: {
                filteredEBooks : [],
                category: null
            }
        })
        .state('navigation.users',{
            url: '/users',
            controller: 'UsersController',
            templateUrl: 'pages/users.html'
        })
        .state('navigation.profile',{
            url: '/profile',
            controller: 'ProfileController',
            templateUrl: 'pages/profile.html'
        })
        .state('navigation.searchEBook',{
            url: '/search',
            controller: 'SearchController',
            templateUrl: 'pages/search.html'
        })

    hljsServiceProvider.setOptions({
        tabReplace: '  '
    });
    hljs.initHighlighting();
});