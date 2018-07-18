/**
 * Created by Jasmina on 19/06/2018.
 */
app.controller('HomeController', function ($scope, $state, $location, $log, $rootScope, $mdSidenav, $mdDialog, $interval, authenticationService) {

    $scope.page = {
        title: 'EBook repository',
        current: 0
    };

    $scope.authService = authenticationService;

});