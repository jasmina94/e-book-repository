/**
 * Created by Jasmina on 26/08/2018.
 */
app.controller('DetailsController', function ($scope, $http, $state, $mdDialog, ebook) {

    $scope.ebook = ebook;

    $scope.close = function () {
        $mdDialog.hide();
    };
});