/**
 * Created by Jasmina on 19/06/2018.
 */
app.controller('NavigationController', function ($scope, $state, $location, $log, $rootScope, $mdSidenav, $mdDialog, authenticationService) {

    $scope.page = {
        title: 'EBook repository',
        current: -1
    };

    $scope.user = authenticationService.getUser();
    $scope.authService = authenticationService;

    $scope.logout = function () {
        authenticationService.logout(function () {
            $state.transitionTo('login');
        }, function () {

        });
    };


    $scope.toggleSidenav = buildToggler('left');

    function buildToggler(navID) {
        return function () {
            $mdSidenav(navID).toggle();
        }
    }

    $scope.isActive = function (pageIndex) {
        return $scope.page.current === pageIndex;
    };


    $scope.emitSearchQuery = function (searchText) {
        $scope.$broadcast('search', {searchText: searchText});
    };

    $scope.goToHome = function () {
        console.log("HOME");
        //$state.transitionTo('navigation.home');
        $mdSidenav('left').close();
    };

    $scope.goToCompanies = function () {
        console.log("COMPANIES");
        //$state.transitionTo('navigation.home');
        $mdSidenav('left').close();
    };
});