/**
 * Created by Jasmina on 19/06/2018.
 */
app.service('categoryService', function($http){
    return {
        read: function(onSuccess, onError){
            $http.get('/api/categories').then(onSuccess, onError);
        },
        create: function(category, onSuccess, onError){
            $http.post('/api/categories', category).then(onSuccess, onError);
        },
        update: function(category, onSuccess, onError){
            $http.patch('/api/categories/' + category.id, category).then(onSuccess, onError);
        },
        delete: function (id, onSuccess, onError) {
            $http.delete('/api/categories/' + id).then(onSuccess, onError);
        }
    }
});