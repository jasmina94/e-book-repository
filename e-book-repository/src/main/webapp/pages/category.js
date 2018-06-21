/**
 * Created by Jasmina on 19/06/2018.
 */
app.controller('CategoryController', function ($scope, $state, $rootScope, $mdDialog, categoryService) {

    $scope.page.current = 0;

    var categories = [];

    var loadData = function () {
        categoryService.read(function (response) {
            $scope.categories = response.data;
            categories = response.data;
        });
    };

    loadData();

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
