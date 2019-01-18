var app = angular.module('Invoices', []);

app.controller('FindAll', function ($scope, $http) {
    $http.get('http://127.0.0.1:8080/invoices').
        then(function (response) {
            $scope.InvoiceDatabase = response.data;
            $scope.empty = $scope.InvoiceDatabase.length;
        });
    $scope.remove = function (id) {
        $http.delete(getBaseApiAddress() + id)
            .then(
                function () {
                    $http.get('http://127.0.0.1:8080/invoices').
                        then(function (response) {
                            $scope.InvoiceDatabase = response.data;
                            $scope.empty = $scope.InvoiceDatabase.length;
                        });
                })
    };
});

app.controller('FindById', function ($scope, $http) {
    $scope.submit = function () {
        $scope.formData = {};
        if ($scope.invoice_id) {
            $http.get(getBaseApiAddress() + $scope.invoice_id)
                .then(
                    function (response) {
                        $scope.InvoiceDatabase = response.data;
                        $scope.formData.answer = 200;
                        console.log(response.data.message);
                    },
                    function (response) {
                        $scope.formData.answer = 400;
                        console.log(response);
                    })
                .catch(function onError(response) {
                    console.log(response);
                });
        }
    }
});

app.controller('AddOrUpdate', function ($scope, $http) {
    $scope.submit = function () {
        if ($scope.content) {
            $scope.formData = {};
            $http.post(getBaseApiAddress(), $scope.content)
                .then(
                    function (response) {
                        $scope.formData.answer = 200;
                        console.log(response.data.message);
                    },
                    function (response) {
                        $scope.formData.answer = 400;
                        console.log(response);
                    })
                .catch(function onError(response) {
                    console.log(response);
                });
        }
    }
    $scope.getJson = function () {
        $scope.content = getSampleNewInvoice();
    }
});

function getBaseApiAddress() {
    return 'http://127.0.0.1:8080/invoices/';
}

function getSampleNewInvoice() {
    var invoice = {
        "id": 1,
        "type": "STANDARD",
        "issueDate": "2018-12-04",
        "dueDate": "2019-01-01",
        "seller": {
            "name": "sampleSeller2",
            "taxIdentificationNumber": "12345678990",
            "accountNumber": {
                "ibanNumber": "PL83620519463926400000847295",
                "localNumber": "83620519463926400000847295"
            },
            "contactDetails": {
                "email": "contact@oracle.com",
                "phoneNumber": "+1234567890",
                "website": "www.oracle.com",
                "address": {
                    "street": "Wyroczni",
                    "number": "13A",
                    "postalCode": "34-760",
                    "city": "Gdynia",
                    "country": "Polska"
                }
            }
        },
        "buyer": {
            "name": "sampleBuyer3",
            "taxIdentificationNumber": "12345678990",
            "accountNumber": {
                "ibanNumber": "PL83620519463926400000847295",
                "localNumber": "83620519463926400000847295"
            },
            "contactDetails": {
                "email": "contact@oracle.com",
                "phoneNumber": "+1234567890",
                "website": "www.oracle.com",
                "address": {
                    "street": "Wyroczni",
                    "number": "13A",
                    "postalCode": "34-760",
                    "city": "Gdynia",
                    "country": "Polska"
                }
            }
        },
        "entries": [
            {
                "item": "Flashlight BX24",
                "quantity": 2,
                "unit": "PIECE",
                "price": 50,
                "vatRate": "VAT_23",
                "netValue": 100,
                "grossValue": 123
            },
            {
                "item": "Swiss Army Knife",
                "quantity": 1,
                "unit": "PIECE",
                "price": 200,
                "vatRate": "VAT_23",
                "netValue": 200,
                "grossValue": 246
            },
            {
                "item": "Engine oil change",
                "quantity": 1,
                "unit": "HOUR",
                "price": 100,
                "vatRate": "VAT_23",
                "netValue": 100,
                "grossValue": 123
            }
        ],
        "totalNetValue": 0,
        "totalGrossValue": 0,
        "comments": "Some comments"
    };
    return JSON.stringify(invoice, null, "\t");
}
