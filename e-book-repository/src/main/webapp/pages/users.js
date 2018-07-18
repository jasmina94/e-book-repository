/**
 * Created by Jasmina on 13/07/2018.
 */
app.controller('UsersController', function ($scope, $state, $rootScope, $mdDialog, userService){

    $scope.page.current = 0;

    var subscribers = [];

    var loadSubscribers = function () {
        userService.read(function (response) {
            $scope.subscribers = response.data;
            subscribers = response.data;
        });
    };

    loadSubscribers();

    $scope.options = {
        rowSelection: true,
        boundaryLinks: true
    };

    $scope.query = {
        order: 'name',
        limit: 5,
        page: 1
    };
});