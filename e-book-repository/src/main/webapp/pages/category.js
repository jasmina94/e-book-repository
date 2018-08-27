/**
 * Created by Jasmina on 19/06/2018.
 */
app.controller('CategoryController', function ($scope, $state, $rootScope, $mdDialog,
                                               categoryService, userService, authenticationService, eBookService) {

    $scope.page.current = 1;
    $scope.authService = authenticationService;

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
            locals: {category: category}
        }).finally(function () {
            loadData();
        });
    };

    $scope.$on('add', function () {
        openForm(null);
    });

    $scope.$on('search', function(event, args) {
        var searchText = args.searchText;
        if (searchText === null || searchText === "") {
            $scope.categories = categories;
            return;
        }
        var criteria = searchText.match(/\S+/g);
        $scope.categories = [];
        for (var i = 0; i < categories.length; i++) {
            for (var j = 0; j < criteria.length; j++) {
                if (categories[i].name.toLowerCase().match(criteria[j].toLowerCase())
                    || categories[i].id === Number(criteria)) {
                    if($scope.categories.indexOf(categories[i]) === -1) {
                        $scope.categories.push(categories[i]);
                    }
                }
            }
        }
    });

    $scope.edit = function (category) {
        var currentUser = authenticationService.getUser();
        if(currentUser != null && currentUser.type === 'ADMIN')
            openForm(category);
    };

    $scope.delete = function (category, event) {
        event.stopPropagation();
        categoryService.delete(category.id, function (response) {
            if (response.data) {
                loadData();
            } else {
                showDeleteErrorMessage();
            }
        }, function (response) {
            console.log('Error');
        });
    };

    $scope.subscribe = function (category, event) {
        // event.stopPropagation();
        // var user = authenticationService.getUser();
        // userService.subscribe(user.id, category.id, function () {
        //     userService.readMe(function (response) {
        //         authenticationService.setUser(response.data);
        //     });
        //     loadData();
        // }, function (response) {
        //     console.log('Error subscribe');
        // });
    };

    $scope.subscribed = function (category) {
        var subscribed = false;
        var user = authenticationService.getUser();
        if (user != null) {
            if (user.category != null && user.category.id == category.id) {
                subscribed = true;
            }
        }
        return subscribed;
    };

    $scope.showBooks = function (category, event) {
        event.stopPropagation();
        var categoryId = category.id;
        eBookService.readForCategory(categoryId, function (response) {
            if(response.data.length != 0){
                var filteredEBooks = response.data;
                $state.transitionTo('navigation.catEBook', {filteredEBooks: filteredEBooks, category: category.name});
            }else {
                showNoEBooksForCategoryMessage(category);
            }

        });
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
    };

    var showNoEBooksForCategoryMessage = function (category) {
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.querySelector('.container')))
                .clickOutsideToClose(false)
                .title("Sorry")
                .textContent("There are no EBooks for category " + category.name + ".")
                .ariaLabel("Sorry")
                .ok('Ok!')
        );
    };
});
