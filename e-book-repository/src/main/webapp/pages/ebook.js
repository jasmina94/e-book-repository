/**
 * Created by Jasmina on 17/07/2018.
 */
app.controller('EBookController', function ($scope, $state, $stateParams, $rootScope, $mdDialog, eBookService, authenticationService) {

    $scope.page.current = 2;
    $scope.authService = authenticationService;
    $scope.noEbooks = false;
    $scope.filteredEBooks = $stateParams.filteredEBooks;
    $scope.forCategory = $stateParams.category;

    var eBooks = [];

    var loadEBooks = function () {
        if ($scope.filteredEBooks != null && $scope.filteredEBooks.length != 0) {
            $scope.eBooks = $scope.filteredEBooks;
            eBooks = $stateParams.filteredEBooks;
            $scope.panelTitle = "EBooks from category " + $scope.forCategory;
        } else {
            eBookService.read(function (response) {
                $scope.eBooks = response.data;
                eBooks = response.data;
                $scope.panelTitle = "EBooks";
                if (eBooks.length === 0) {
                    $scope.noEbooks = true;
                }
            });
        }
    };

    loadEBooks();

    $scope.$on('add', function () {
        openForm(null, false);
    });

    var openForm = function (eBook, successfulUpload) {
        $mdDialog.show({
            parent: angular.element(document.body),
            templateUrl: 'dialogs/ebookForm.html',
            controller: 'EBookFormController',
            locals: {
                eBook: eBook,
                successfulUpload: successfulUpload
            }
        });
    };

    var showCommercialDialog = function () {
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.querySelector('.container')))
                .clickOutsideToClose(false)
                .title("Join us")
                .textContent("To be able to download EBook, please sign up and subscribe to wished EBook category!")
                .ariaLabel("Commercial dialog")
                .ok('Ok!')
        );
    };

    var showPermissionDialog = function () {
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.querySelector('.container')))
                .clickOutsideToClose(false)
                .title("Download unavailable")
                .textContent("You are not subscribed to this EBook category. Download is unavailable!")
                .ariaLabel("Permission dialog")
                .ok('Ok!')
        );
    };

    $scope.download = function (eBook, event) {
        event.stopPropagation();
        var filename = eBook.filename;
        var category = eBook.category;
        var canDownload = authenticationService.canDownload(category)
        if (canDownload.success) {
            var aElement = document.createElement("a");
            var fileUrl = "/eBooks/" + filename;
            aElement.href = fileUrl;
            aElement.download = filename;
            aElement.click();
        } else {
            if (canDownload.dialogType === "permission") {
                showPermissionDialog();
            } else {
                showCommercialDialog();
            }
        }
    };


    $scope.delete = function (eBook, event) {
        event.stopPropagation();
        eBookService.delete(eBook.id, function (response) {
            loadEBooks();
        });
    };

    $scope.edit = function (eBook) {
        if (authenticationService.getUser().type === 'ADMIN')
            openForm(eBook, true);
    };

    $scope.goToSearch = function () {
        $state.transitionTo('navigation.searchEBook');
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