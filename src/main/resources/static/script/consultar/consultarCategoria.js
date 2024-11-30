var table;

$(document).ready(function() {
    var table = $('#tableCategorias').DataTable({
		columns: [
		        { data: 'id' },
		        { data: 'name' },
		        { data: 'active' }
		    ],
        "paging": true,
        "pageLength": 10,
        "language": {
            "sEmptyTable": "Nenhum registro encontrado",
            "sInfo": "Mostrando de _START_ até _END_ de _TOTAL_ registros",
            "sInfoEmpty": "Mostrando 0 até 0 de 0 registros",
            "sSearch": "Pesquisar",
            "oPaginate": {
                "sNext": "Próximo",
                "sPrevious": "Anterior"
            }
        }
    });

$("#tableCategorias tbody").on('click', 'tr', function() {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
            $('#excluir').prop('disabled', true);
            $('#editar').prop('disabled', true);
        } else {
            $('#tableCategorias tbody tr.selected').removeClass('selected');
            $(this).addClass('selected');
            $('#excluir').prop('disabled', false);
            $('#editar').prop('disabled', false);
        }
    });


	function getId() {
		var row = $('#tableCategorias tbody tr.selected');
		if (row.length) {
			return row.data('id');
		}
		return null;
	}

	$("#editar").on('click', function() {
		const id = getId();
		if (id) {
			window.location.href = '/product/editar/' + id;
		} else {
			alert('Por favor, selecione um registro para editar.');
		}
	});

	$('#excluir').on('click', function() {
	        var productId = getId();
	        if (productId) {
	            const confirmDelete = confirm("Deseja realmente excluir o produto?");
	            if (confirmDelete) {
	                $.ajax({
	                    url: '/product/deletar/' + productId,
	                    method: 'POST',
	                    success: function(response) {
	                        alert('Produto excluído com sucesso!');
	                        table.ajax.reload();
	                    },
	                    error: function() {
	                        alert('Erro ao excluir o produto.');
	                    }
	                });
	            }
	        } else {
	            alert('Selecione um produto para excluir.');
	        }
	    });
	});