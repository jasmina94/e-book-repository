/**
 * Created by Jasmina on 18/07/2018.
 */
app.controller('UserFormController', function ($scope, $http, $state, $mdDialog, userService, categoryService, user) {

    var editingMode = user !== null;
    if (editingMode) {
        $scope.user = JSON.parse(JSON.stringify(user));
        $scope.disabled = true;
    } else {
        $scope.user = {};
    }

    $scope.userTypes = {};
    $scope.userTypes["ADMIN"] = "Administrator";
    $scope.userTypes["SUBSCRIBER"] = "Subscriber";


    categoryService.read(function (response) {
        $scope.categories = response.data;
    });

    $scope.queryCategories = function (searchText) {
        if (searchText === null) {
            return $scope.categories;
        }
        var queryResults = [];
        for (var i=0; i < $scope.categories.length; i++) {
            if ($scope.categories[i].name.toLowerCase().match(searchText.toLowerCase())) {
                queryResults.push($scope.categories[i])
            }
        }
        return queryResults;
    };

    $scope.submit = function () {
        if (!editingMode) {
            userService.create($scope.user, function (response) {
                if(response.data != ""){
                    $scope.close();
                }else {
                    $scope.userForm.username.$error.uniqueError = true;
                }
            }, function (response) {
                console.log('Error user creating');
            });
        } else {
            userService.update($scope.user, function (response) {
                if(response.data != ""){
                    $scope.close();
                    $scope.disabled = false;
                }else {
                    $scope.userForm.username.$error.uniqueError = true;
                }
            }, function (response) {
                console.log('Error user update');
            });
        }
    };

    $scope.close = function () {
        $mdDialog.hide();
    }
});