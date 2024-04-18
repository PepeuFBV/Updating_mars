.include "macros.asm"

.data
.text
	# o processo principal cria 3 processos filhos
	SyscallFork(Programa1)
	SyscallFork(Programa2)
	SyscallFork(Idle)
	
	# o processo principal para de executar e chama o escalonador
	SyscallProcessChange
	
	# encerrando o processo principal
	SyscallProcessTerminate

# loop infinito chamando o escalonador
Idle:
	loop:
		SyscallProcessChange
		j loop

# loop chama o escalonador 10 vezes e encerra
Programa1:
	addi $s1, $zero, 1 # valor inicial do contador
	addi $s2, $zero, 10 # valor limite do contador
	loop1: 	addi $s1, $s1, 1
		beq $s1, $s2, fim1
		SyscallProcessChange
		j loop1
	fim1:	SyscallProcessTerminate

# loop chama o escalonador 10 vezes e encerra
Programa2:
	addi $s1, $zero, -1 # valor inicial do contador
	addi $s2, $zero, -10 # valor limite do contador
	loop2: 	addi $s1, $s1, -1
		beq $s1, $s2, fim2
		SyscallProcessChange
		j loop2
	fim2:	SyscallProcessTerminate