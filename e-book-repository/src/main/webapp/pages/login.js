/**
 * Created by Jasmina on 19/06/2018.
 */
app.controller('LoginController', function ($scope, $state, $http, $mdDialog, authenticationService) {

    $scope.submit = function () {
        authenticationService.login($scope.user, function () {
            $http.get('/api/users/me')
                .success(function (user) {
                    authenticationService.setUser(user);
                    $state.transitionTo('navigation.home');
                })
                .error(error);
        }, error);
    };

    var error = function () {
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.body))
                .title('Login failed!')
                .content('Wrong username or password.')
                .ok('Ok')
        );
    };

    $scope.create = function () {
        $mdDialog.show({
            parent: angular.element(document.body),
            templateUrl: 'dialog/createAccount.html',
            controller: 'CreateAccountController'
        }).finally(function() {
        });
    };

    $scope.close = function () {
        $state.transition('navigation.home');
    };
});