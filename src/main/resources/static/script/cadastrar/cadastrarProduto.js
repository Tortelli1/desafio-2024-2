$(document).ready(function() {
            $('#name').on('blur', function() {
                var productName = $('#name').val();
                if (productName) {
                    $.ajax({
                        url: '/product/apiFetchProduct',
                        method: 'GET',
                        data: { name: productName },
                        success: function(data) {
                            $('#description').val(data.description);
                            $('#price').val(data.price);
                            $('#stock').val(data.stock);
                            $('#rating').val(data.rating);
                            $('#sku').val(data.sku);
                            $('#weight').val(data.weight);
                        },
                        error: function() {
                            alert('Erro ao buscar dados da API.');
                        }
                    });
                }
            });
        });