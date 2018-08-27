/**
 * Created by Jasmina on 18/08/2018.
 */
app.controller('EBookFormController', function ($scope, $http, $state, $mdDialog, eBookService,
            languageService, categoryService, authenticationService, eBook, successfulUpload) {

    var editingMode = eBook !== null;
    if (editingMode) {
        $scope.eBook = JSON.parse(JSON.stringify(eBook));
        $scope.disabled = true;
        $scope.successfulUpload = successfulUpload;
    } else {
        $scope.eBook = {};
    }

    languageService.read(function (response) {
        $scope.languages = response.data;
    });

    categoryService.read(function (response) {
        $scope.categories = response.data;
    });

    $scope.queryLanguages = function (searchText) {
        if (searchText === null) {
            return $scope.languages;
        }
        var queryResults = [];
        for (var i = 0; i < $scope.languages.length; i++) {
            if ($scope.languages[i].name.toLowerCase().match(searchText.toLowerCase())) {
                queryResults.push($scope.languages[i])
            }
        }
        return queryResults;
    };

    $scope.queryCategories = function (searchText) {
        if (searchText === null) {
            return $scope.categories;
        }
        var queryResults = [];
        for (var i = 0; i < $scope.categories.length; i++) {
            if ($scope.categories[i].name.toLowerCase().match(searchText.toLowerCase())) {
                queryResults.push($scope.categories[i])
            }
        }
        return queryResults;
    };

    $scope.uploadFile = function (files) {
        var fd = new FormData();
        fd.append('file', files[0]);
        var uploadUrl = '/api/ebooks/upload';

        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).success(function (response) {
            showBaseData(response);
        }).error(function () {
            showErrorUploadDialog();
        });
    };

    $scope.submit = function () {
        if (!editingMode) {
            var currentUser = authenticationService.getUser();
            $scope.eBook.cataloguer = currentUser;
            eBookService.create($scope.eBook, function (response) {
                //$scope.close();
                //$scope.disabled = false;
                showSuccessfulSavingDialog(response.data);
            }, function (response) {
                showErrorSavingDialog();
            });
        } else {
            eBookService.update($scope.eBook, function (response) {
                $scope.close();
                $scope.disabled = false;
                showSuccessfulSavingDialog(response.data);
            }, function (response) {
                showErrorSavingDialog();
            });
        }
    };


    var showErrorUploadDialog = function () {
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.querySelector('.container')))
                .clickOutsideToClose(false)
                .title("Error uploading file")
                .textContent("Server error occurred while uploading file!")
                .ariaLabel("Alert upload error")
                .ok('Ok!')
        );
    };

    var showErrorSavingDialog = function () {
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.querySelector('.container')))
                .clickOutsideToClose(false)
                .title("Error saving data")
                .textContent("Server error occurred while saving data!")
                .ariaLabel("Alert upload error")
                .ok('Ok!')
        );
    };

    var showSuccessfulSavingDialog = function (ebook) {
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.querySelector('.container')))
                .clickOutsideToClose(false)
                .title("Successfully saved data")
                .textContent("You have successfully saved EBook - "+ ebook.title + " to repository!")
                .ariaLabel("Successfully saved data")
                .ok('Ok!')
        ).finally(function () {
            $state.reload();
        });
    };

    var showBaseData = function (eBookBase) {
        $scope.successfulUpload = true;
        $scope.eBook.author = eBookBase.author;
        $scope.eBook.title = eBookBase.title;
        $scope.eBook.filename = eBookBase.filename;
        $scope.eBook.keywords = eBookBase.keywords;
        $scope.eBook.mime = "application/pdf";
    };



    $scope.close = function () {
        $mdDialog.hide();
    }

    $scope.back = function(){
        var filename = $scope.eBook.filename;
        if(filename !== ""){
            eBookService.deleteFile(filename, function (response) {
                console.log(response);
                $scope.successfulUpload = false;
                angular.element("input[type='file']").val(null);
            }, function (response) {
                console.log(response);
            });
        }
    }
});