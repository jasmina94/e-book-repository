/**
 * Created by Jasmina on 19/06/2018.
 */
app.controller('CategoryController', function ($scope, $state, $rootScope, $mdDialog, categoryService, userService,authenticationService) {

    $scope.page.current = 1;

    var categories = [];

    var loadData = function () {
        categoryService.read(function (response) {
            $scope.categories = response.data;
            categories = response.data;
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
        if(authenticationService.getUser().type === 'ADMIN')
            openForm(category);
    };

    $scope.delete = function (category, event) {
        event.stopPropagation();
        categoryService.delete(category.id, function (response) {
            if(response.data){
                loadData();
            }else {
                showDeleteErrorMessage();
            }

        },function (response) {
            console.log('Error');
        });
    };

    $scope.subscribe = function (category, event) {
      event.stopPropagation();
      var user = authenticationService.getUser();
      userService.subscribe(user.id, category.id, function () {
          userService.readMe(function (response) {
              authenticationService.setUser(response.data);
          });
          loadData();
      },function (response) {
         console.log('Error subscribe');
      });
    };

    $scope.subscribed = function (category) {
       var subscribed = false;
       var user = authenticationService.getUser();
       if(user != null){
           if(user.category != null && user.category.id == category.id){
               subscribed = true;
           }
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

    var showDeleteErrorMessage = function () {
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.querySelector('.container')))
                .clickOutsideToClose(false)
                .title("Delete category warning")
                .textContent("This category is connected to some e-books. It can't be deleted!")
                .ariaLabel("Alert delete category")
                .ok('Ok!')
        );
    }
});
