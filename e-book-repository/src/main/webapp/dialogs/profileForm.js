/**
 * Created by Jasmina on 19/07/2018.
 */
app.controller('ProfileFormController', function ($scope, $http, $state, $mdDialog, userService, authenticationService) {

    $scope.user = authenticationService.getUser();
    $scope.disabled = true;

    $scope.close = function () {
        $mdDialog.hide();
    };

    $scope.submit = function () {
        userService.update($scope.user, function (response) {
            if(response.data != null){
                $scope.close();
                $scope.disabled = false;
                $scope.user = response.data;
                authenticationService.setUser(response.data);
                $state.reload();
            }else {
                $scope.profileForm.username.$error.uniqeError = response.status === 409;
            }
        }, function (response) {
            $scope.profileForm.username.$error.globalError = true;
        });
    }
});