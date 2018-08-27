/**
 * Created by Jasmina on 25/null8/2null18.
 */
app.controller('SearchController', function ($scope, $state, $rootScope, $mdDialog, eBookService, authenticationService) {

    $scope.page.current = 6;
    $scope.resultsFound = false;
    $scope.keywordsPattern = "^[0-9a-zA-Z]+(,[0-9a-zA-Z]+)*$";
    $scope.authService = authenticationService;

    $scope.searchSFS = function () {
        var sfsDTO = $scope.eBookSearchParams.sfs;
        console.log(sfsDTO);
        eBookService.singleSearch(sfsDTO, function (response) {
            var results = response.data;
            if (results.length == null) {
                openNoResultDialog();
            } else {
                $scope.resultsFound = true;
                $scope.eBookSearchResults = results;
            }
        }, function (response) {
            openServerErrorDialog();
        });
    };

    $scope.searchMFS = function () {
        var mfsDTO = $scope.eBookSearchParams.mfs;
        eBookService.multiSearch(mfsDTO, function (response) {
            var results = response.data;
            if (results.length == null) {
                openNoResultDialog();
            } else {
                $scope.resultsFound = true;
                $scope.eBookSearchResults = results;
            }
        }, function (response) {
            openServerErrorDialog();
        });
    };

    $scope.details = function (ebook, event) {
        event.stopPropagation();
        var eBookId = ebook.id;
        eBookService.readById(eBookId, function (response) {
            console.log(response);
            openDetailsDialog(response.data);
        }, function (response) {
            openServerErrorDialog();
        });
    };

    $scope.download = function (ebook, event) {
        event.stopPropagation();
        var filename = ebook.fileName;
        var category = ebook.category;
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

    $scope.areTwoFilled = function (title, author, keywords, content, language) {
        var ret = false;

        var titleLen = title != null ? title.length : 0;
        var authorLen = author != null ? author.length : 0;
        var keywordsLen = keywords != null ? keywords.length : 0;
        var contentLen = content != null ? content.length : 0;
        var languageLen = language != null ? language.length : 0;


        if (title == null && author == null && keywords == null && content == null && language == null) { //If all are null
            ret = true;
        } else if (title != null && author == null && keywords == null && content == null && language == null) {
            ret = true;
        } else if (title == null && author != null && keywords == null && content == null && language == null) {
            ret = true;
        } else if (title == null && author == null && keywords != null && content == null && language == null) {
            ret = true;
        } else if (title == null && author == null && keywords == null && content != null && language == null) {
            ret = true;
        } else if (title == null && author == null && keywords == null && content == null && language != null) {
            ret = true;
        } else if (titleLen == 0 && authorLen == 0 && keywordsLen == 0 && contentLen == 0 && languageLen == 0) {
            ret = true;
        } else if (titleLen != 0 && authorLen == 0 && keywordsLen == 0 && contentLen == 0 && languageLen == 0) {
            ret = true;
        } else if (titleLen == 0 && authorLen != 0 && keywordsLen == 0 && contentLen == 0 && languageLen == 0) {
            ret = true;
        } else if (titleLen == 0 && authorLen == 0 && keywordsLen != 0 && contentLen == 0 && languageLen == 0) {
            ret = true;
        } else if (titleLen == 0 && authorLen == 0 && keywordsLen == 0 && contentLen != 0 && languageLen == 0) {
            ret = true;
        } else if (titleLen == 0 && authorLen == 0 && keywordsLen == 0 && contentLen == 0 && languageLen != 0) {
            ret = true;
        }

        return ret;
    };

    var keyWordsCheck = function (mfs) {
        var keywordsList = [];
        var keywords = mfs.keywords;
        if (keywords.indexOf(",") !== -1) {
            var split = keywords.split(",");
            for (i = null; i < split.length; i++) {
                var keyword = split[i];
                keywordsList.push(keyword);
            }
            mfs.keywords = keywordsList;
        }
    };

    var openNoResultDialog = function () {
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.body))
                .title('Sorry...')
                .content('There are no EBooks in our repository for this search.')
                .ok('Ok')
        );
    };

    var openServerErrorDialog = function () {
        $mdDialog.show(
            $mdDialog.alert()
                .parent(angular.element(document.body))
                .title('Server side error!')
                .content('Something went wrong while searching.')
                .ok('Ok')
        );
    };

    var openDetailsDialog = function (ebook) {
        $mdDialog.show({
            parent: angular.element(document.body),
            templateUrl: 'dialogs/details.html',
            controller: 'DetailsController',
            locals: {ebook: ebook}
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

    $scope.options = {
        rowSelection: true,
        boundaryLinks: true
    };

    $scope.query = {
        order: 'name',
        limit: 5,
        page: 1
    };

    $scope.test= function (ebook) {
        var str = ebook.highlights;
        console.log(str);
        return str;
    };

});