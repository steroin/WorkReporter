var module = angular.module('solution', []);
module.directive
module.controller('solutionChooser', function($scope, $http) {
    $http.get('solution/managedSolutions').then(function(data) {
        $scope.solutionChooserData = data.data;
    });
});