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
            $state.transitionTo('navigation.home');
            localStorage.clear();
            $scope.user = null;
        }, function () {

        });
    };

    $scope.login = function () {
      $state.transitionTo('login');
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
        $state.transitionTo('navigation.home');
        $mdSidenav('left').close();
    };

    $scope.goToUserManagement = function () {
        $state.transitionTo('navigation.users');
        $mdSidenav('left').close();
    };

    $scope.goToProfileManagement = function () {
        $state.transitionTo('navigation.profile');
        $mdSidenav('left').close();
    };

    $scope.goToCategories = function () {
        $state.transitionTo('navigation.category');
        $mdSidenav('left').close();
    };

    $scope.goToEBooks = function () {
        $state.transitionTo('navigation.ebook');
        $mdSidenav('left').close();
    };

    $scope.showAddButton = function () {
        var show = false;
        var page = $scope.page;
        if((page.current == 1 || page.current == 2  || page.current == 5)
            && authenticationService.isAdmin()){
            show = true;
        }
        return show;
    };

    $scope.showSearchOption = function () {
      var show = false;
      var page = $scope.page;
      if(page.current == 1 || page.current == 5){
          show = true;
      }
      return show;
    };

    $scope.add = function () {
        $scope.$broadcast('add');
        $scope.showSearch = false;
        $scope.emitSearchQuery(null);
    };

    $scope.radiBre = function () {
        $scope.showSearch = true;
    };
});