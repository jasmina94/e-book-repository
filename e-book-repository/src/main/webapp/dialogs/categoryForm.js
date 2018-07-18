/**
 * Created by Jasmina on 18/07/2018.
 */
app.controller('CategoryFormController', function ($scope, $http, $state, $mdDialog, categoryService, category) {

    var editingMode = category !== null;
    if (editingMode) {
        $scope.category = JSON.parse(JSON.stringify(category));
        $scope.disabled = true;
    } else {
        $scope.category = {};
    }

    $scope.submit = function () {
        if (!editingMode) {
            categoryService.create($scope.category, function () {
                $scope.close();
            }, function (response) {
                console.log('Error');
            });
        } else {
            categoryService.update($scope.category, function () {
                $scope.close();
                $scope.disabled = false;
            }, function (response) {
                console.log('Error');
            });
        }
    };
    
    $scope.close = function () {
        $mdDialog.hide();
    }
});