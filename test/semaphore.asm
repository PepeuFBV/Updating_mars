.include "macros.asm"

.data
	mutex: .word 1
	empty: .word 0
	full: .word 10
	buffer: .word 1, 2, 3, 4, 5, 6, 7, 8, 9, 10

.text
main:
	# Cria os semaforos
	SyscallCreateSemaphore(mutex)
	SyscallCreateSemaphore(empty)
	SyscallCreateSemaphore(full)
	
	SyscallFork(produtor)		# Cria o processo produtor
	SyscallFork(consumidor)		# Cria o processo consumidor
	
	SyscallProcessTerminate		# Termina o processo principal
	
produtor:
	la $t0, buffer      # Carrega o endereco do buffer
	loop1:
		SyscallDownSemaphore(empty)    # Verifica se pode produzir
		SyscallDownSemaphore(mutex)    # Entra na regiao critica (se mutex > 0)
		
		addi $s0, $s0, 1    # Produz um item (produz valores de 1 a 10)
		lw $t1, full		# Carrega o valor de full
		mul $t1, $t1, 4		# Multiplica full por 4 bytes = quantidade de inteiros no buffer
		add $t2, $t1, $t0	# Salva o deslocamento do endereco do buffer em $t2 (topo)
		sw $s0, 0($t2)      # Armazena o valor do registrador $s0 no topo do buffer
		
		SyscallUpSemaphore(mutex)    # Libera acesso ao buffer
		SyscallUpSemaphore(full)     # Incrementa o contador de itens no buffer
		j loop1
	fim1:	SyscallProcessTerminate

consumidor:
	la $t0, buffer      # Carrega o endereco do buffer
	loop2:
		SyscallDownSemaphore(full)    # Verifica se pode consumir
		SyscallDownSemaphore(mutex)    # Entra na regiao critica (se mutex > 0)
		
		lw $t1, full		# Carrega o valor de full
		mul $t1, $t1, 4		# Multiplica full por 4 bytes = quantidade de inteiros no buffer
		add $t2, $t1, $t0	# Salva o deslocamento do endere�o do buffer em $t2 (topo)
		sw $0, 0($t2)       # Consome um item do buffer salvando o valor 0
		
		SyscallUpSemaphore(mutex)    # Libera acesso ao buffer
		SyscallUpSemaphore(empty)    # Incrementa o contador de espacos vazios no buffer
		j loop2	
	fim2:	SyscallProcessTerminate
