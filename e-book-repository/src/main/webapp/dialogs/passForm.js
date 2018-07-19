/**
 * Created by Jasmina on 19/07/2018.
 */
app.controller('PasswordFormController', function ($scope, $http, $state, $mdDialog, userService, authenticationService, user) {

    $scope.close = function () {
        $mdDialog.hide();
    };

    $scope.submit = function () {
        var oldPass = $scope.pass.oldPassword;
        var newPass = $scope.pass.newPassword;
        var repPass = $scope.pass.repeatedPassword;
        if(oldPass !== user.password){
            $scope.passForm.oldPassword.$error.matchingError = true;
        }else if(newPass !== repPass){
            $scope.passForm.repeatedPassword.$error.matchingError = true;
        }else {
            user.password = newPass;
            userService.updatePass(user.id, $scope.pass, function (response) {
                if(response.data){
                    $scope.close();
                    $scope.disabled = false;
                    userService.readMe(function(response) {
                        $scope.user = response.data;
                        authenticationService.setUser(response.data);
                    });
                }else {
                    $scope.passForm.repeatedPassword.$error.globalError = true;
                }
            }, function (response) {
                console.log('Error user update');
            });
        }
    };
});