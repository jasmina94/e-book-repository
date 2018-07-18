/**
 * Created by Jasmina on 19/06/2018.
 */
app.controller('CategoryController', function ($scope, $state, $rootScope, $mdDialog, categoryService, userService,authenticationService) {

    $scope.page.current = 0;

    var categories = [];

    var loadData = function () {
        categoryService.read(function (response) {
            $scope.categories = response.data;
            categories = response.data;
        });
        userService.readMe(function (response) {
            authenticationService.setUser(response.data);
        });
    };

    loadData();

    var openForm = function (category) {
        $mdDialog.show({
            parent: angular.element(document.body),
            templateUrl: 'dialogs/categoryForm.html',
            controller: 'CategoryFormController',
            locals: { category: category}
        }).finally(function () {
            loadData();
        });
    };

    $scope.$on('add', function() {
        openForm(null);
    });

    $scope.edit = function (category) {
        openForm(category);
    };

    $scope.delete = function (category, event) {
        event.stopPropagation();
        categoryService.delete(category.id, function () {
            loadData();
        },function (response) {
            console.log('Error');
        });
    };

    $scope.subscribe = function (category, event) {
      event.stopPropagation();
      var user = authenticationService.getUser();
      userService.subscribe(user.id, category.id, function () {
          loadData();
      },function (response) {
         console.log('Error subscribe');
      });
    };

    $scope.subscribed = function (category) {
       var subscribed = false;
       var user = authenticationService.getUser();
       if(user.category != null && user.category.id == category.id){
           subscribed = true;
       }
       return subscribed;
    };

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
