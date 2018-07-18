/**
 * Created by Jasmina on 13/07/2018.
 */
app.service('userService', function($http){
    return {
        read: function(onSuccess, onError){
            $http.get('/api/users').then(onSuccess, onError);
        },
        readMe: function (onSuccess, onError) {
            $http.get('/api/users/me').then(onSuccess, onError);
        },
        readSubs: function (onSuccess, onError) {
            $http.get('/api/users/subs').then(onSuccess, onError);
        },
        create: function(user, onSuccess, onError){
            $http.post('/api/users', user).then(onSuccess, onError);
        },
        update: function(user, onSuccess, onError){
            $http.patch('/api/users/' + user.id, user).then(onSuccess, onError);
        },
        delete: function (id, onSuccess, onError) {
            $http.delete('/api/users/' + id).then(onSuccess, onError);
        },
        subscribe: function (userId, categoryId, onSuccess, onError) {
            $http.post('/api/users/' + userId + '/' + categoryId).then(onSuccess, onError);
        }
    }
});