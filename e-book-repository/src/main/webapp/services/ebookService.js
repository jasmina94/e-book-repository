/**
 * Created by Jasmina on 18/07/2018.
 */
app.service('eBookService', function ($http) {
    return {
        read: function (onSuccess, onError) {
            $http.get('/api/ebooks').then(onSuccess, onError);
        },
        readById: function (id, onSuccess, onError) {
            $http.get('/api/ebooks/' + id).then(onSuccess, onError);
        },
        readForCategory: function (categoryId, onSuccess, onError) {
            $http.get('/api/ebooks/category/' + categoryId).then(onSuccess, onError);
        },
        uploadFile: function (data, onSuccess, onError) {
            $http.post('/api/ebooks/upload', data).then(onSuccess, onError);
        },
        create: function (eBook, onSuccess, onError) {
            $http.post('/api/ebooks', eBook).then(onSuccess, onError);
        },
        update: function (eBook, onSuccess, onError) {
            $http.patch('/api/ebooks/' + eBook.id, eBook).then(onSuccess, onError);
        },
        delete: function (id, onSuccess, onError) {
            $http.delete('/api/ebooks/' + id).then(onSuccess, onError);
        },
        deleteFile: function (filename, onSuccess, onError) {
            $http.delete('/api/ebooks/file/' + filename).then(onSuccess, onError);
        },
        download: function (id, onSuccess, onError) {
            $http.get('/api/ebooks/download/' + id).then(onSuccess, onError);
        },
        singleSearch: function (sfsDTO, onSuccess, onError) {
            $http.post('/api/ebooks/ssearch', sfsDTO).then(onSuccess, onError);
        },
        multiSearch: function (mfsDTO, onSuccess, onError) {
            $http.post('/api/ebooks/msearch', mfsDTO).then(onSuccess, onError);
        }
    }
});