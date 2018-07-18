/**
 * Created by Jasmina on 16/06/2018.
 */
app.service('authenticationService', function ($http, $window) {

    var LOCAL_STORAGE_KEY = 'user';
    var LOCAL_STORAGE_INSTANCE = $window.localStorage;

    return {
        login: function (user, successCallback, errorCallback) {
            $http.post('/api/login', user, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                }
            }).success(successCallback).error(errorCallback);
        },
        logout: function (successCallback, errorCallback) {
            var loggedInUser = this.getUser();
            if (loggedInUser) {
                $http.post("/api/logout", loggedInUser.id).success(successCallback).error(errorCallback);
            }
        },
        getUser: function () {
            if (LOCAL_STORAGE_INSTANCE) {
                var loggedInUser = LOCAL_STORAGE_INSTANCE.getItem(LOCAL_STORAGE_KEY);
                if (loggedInUser) {
                    return JSON.parse(loggedInUser);
                }
            }
        },
        setUser: function (user) {
            if (LOCAL_STORAGE_INSTANCE && user) {
                LOCAL_STORAGE_INSTANCE.setItem(LOCAL_STORAGE_KEY, JSON.stringify(user));
            }
        },
        register: function (user, successCallback, errorCallback) {
            $http.post("/api/register", user).success(successCallback).error(errorCallback);
        },
        removeUser: function () {
            LOCAL_STORAGE_INSTANCE && LOCAL_STORAGE_INSTANCE.removeItem(LOCAL_STORAGE_KEY);
        },
        isLoggedIn: function(){
            if(this.getUser() != null){
                return true;
            }else {
                return false;
            }
        },
        isAdmin: function () {
            if(this.getUser() != null){
                if(this.getUser().type === 'ADMIN'){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }

        },
        isSubscriber: function () {
            if(this.getUser() != null) {
                if (this.getUser().type === 'SUBSCRIBER') {
                    return true;
                } else {
                    return false;
                }
            }
        },
        isVisitor: function () {
            if(this.getUser() == null){
                return true;
            }else {
                return false;
            }
        },
        hasNoSubscription: function () {
            if(this.getUser() != null){
                var user = this.getUser();
                if(user.category == null && user.type === 'SUBSCRIBER'){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        },
        hasSubscription: function () {
            if(this.getUser() != null){
                var user = this.getUser();
                if(user.category != null && user.type === 'SUBSCRIBER'){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }
    }
});