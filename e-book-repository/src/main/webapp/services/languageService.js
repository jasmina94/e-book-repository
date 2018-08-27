/**
 * Created by Jasmina on 18/08/2018.
 */
app.service('languageService', function($http){
    return {
        read: function(onSuccess, onError){
            $http.get('/api/languages').then(onSuccess, onError);
        },
        create: function(language, onSuccess, onError){
            $http.post('/api/languages', language).then(onSuccess, onError);
        },
        update: function(language, onSuccess, onError){
            $http.patch('/api/languages/' + language.id, language).then(onSuccess, onError);
        },
        delete: function (id, onSuccess, onError) {
            $http.delete('/api/languages/' + id).then(onSuccess, onError);
        }
    }
});