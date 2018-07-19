/**
 * Created by Jasmina on 13/07/2018.
 */
app.controller('ProfileController', function ($scope, $state, $rootScope, $mdDialog, authenticationService, userService){

    $scope.page.current = 4;

    $scope.categoryName = ($scope.user.category === null) ? "All" : $scope.user.category.name;

    $scope.passChange = function () {
        openPassForm($scope.user);
    };

    $scope.otherChange = function () {
        openProfileForm($scope.user);
    };

    var openPassForm = function (user) {
        $mdDialog.show({
            parent: angular.element(document.body),
            templateUrl: 'dialogs/passForm.html',
            controller: 'PasswordFormController',
            locals: { user: user}
        });
    }

    var openProfileForm = function (user) {
        $mdDialog.show({
            parent: angular.element(document.body),
            templateUrl: 'dialogs/profileForm.html',
            controller: 'ProfileFormController',
            locals: { user: user}
        });
    }
});