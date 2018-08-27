/**
 * Created by Jasmina on 13/07/2018.
 */
app.controller('UsersController', function ($scope, $state, $rootScope, $mdDialog, userService, authenticationService){

    $scope.page.current = 5;

    var users = [];
    var subscribers = [];

    $scope.typesOfUser = {};
    $scope.typesOfUser['ADMIN'] = "Administrator";
    $scope.typesOfUser['SUBSCRIBER'] = "Subscriber";

    var loadUsers = function () {
        userService.read(function (response) {
            $scope.users = filter(response.data);
            users = $scope.users;
            filterSubscribers(response.data);
        });
    };

    var filterSubscribers = function (users) {
        for(var i=0; i<users.length; i++){
            var user = users[i];
            if(user.type === 'SUBSCRIBER'){
                subscribers.push(user);
            }
        }
    };

    var filter = function (users) {
        var me = authenticationService.getUser();
        for(var i=0; i<users.length; i++){
            var user = users[i];
            if(user.id === me.id){
                users.splice(i, 1);
            }
        }
        return users;
    };


    loadUsers();

    var openForm = function (user) {
        $mdDialog.show({
            parent: angular.element(document.body),
            templateUrl: 'dialogs/userForm.html',
            controller: 'UserFormController',
            locals: { user: user}
        }).finally(function () {
            loadUsers();
        });
    };

    $scope.$on('add', function() {
        openForm(null);
    });

    $scope.$on('search', function(event, args) {
        var searchText = args.searchText;
        if (searchText === null || searchText === "") {
            $scope.users = users;
            return;
        }
        var criteria = searchText.match(/\S+/g);
        $scope.users = [];
        for (var i = 0; i < users.length; i++) {
            for (var j = 0; j < criteria.length; j++) {
                if (users[i].firstname.toLowerCase().match(criteria[j].toLowerCase())
                    || users[i].lastname.toLowerCase().match(criteria[j].toLowerCase())
                    || users[i].username.toLowerCase().match(criteria[j].toLowerCase())
                    || users[i].category.name.toLowerCase().match(criteria[j].toLowerCase())
                    || users[i].type.toLowerCase().match(criteria[j].toLowerCase())
                    || users[i].id === Number(criteria)) {
                    if($scope.users.indexOf(users[i]) === -1) {
                        $scope.users.push(users[i]);
                    }
                }
            }
        }
    });

    $scope.edit = function (user) {
        if(authenticationService.getUser().type === 'ADMIN')
            openForm(user);
    };

    $scope.delete = function (user, event) {
        event.stopPropagation();
        userService.delete(user.id, function () {
            loadUsers();
        },function (response) {
            console.log('Error');
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
});