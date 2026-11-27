/* use strict */
var app = angular.module("TabviewApp", []);

app.service("tabviewService", function ($http, $q)
{ 
	var deferred = $q.defer();
	$http.get('data.json').then(function (data)
	{
		deferred.resolve(data);
	});

	this.getRows = function ()
	{
		return deferred.promise;
	}
})

.controller("tabviewCtrl", function ($scope, tabviewService)
{
	var promise = tabviewService.getRows();
	promise.then(function (data)
	{
		$scope.rows = data.data;
		$scope.reverseSort = false;
		console.log($scope.rows);
	});
	
	$scope.order = function(sortType) {
	  if ( $scope.sortType == sortType ) {
	   $scope.sortReverse = !$scope.sortReverse
    } else {
      $scope.sortType = sortType;
      $scope.sortReverse = false;
    }	
  };
})
