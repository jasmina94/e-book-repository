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
            $scope.users = response.data;
            users = response.data;
            filterSubscribers(users);
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