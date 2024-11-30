var table;

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

$(document).ready(function() {
	var table = $('#tableProdutos').DataTable({
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

	$("#tableProdutos tbody").on('click', 'tr', function() {
		if ($(this).hasClass('selected')) {
			$(this).removeClass('selected');
			$('#excluir').prop('disabled', true);
			$('#editar').prop('disabled', true);
		} else {
			$('#tableProdutos tbody tr.selected').removeClass('selected');
			$(this).addClass('selected');
			$('#excluir').prop('disabled', false);
			$('#editar').prop('disabled', false);
		}
	});


	function getId() {
		var row = $('#tableProdutos tbody tr.selected');
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
					method: 'DELETE',
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