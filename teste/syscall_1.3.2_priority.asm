.include "macros.asm"

.data
.text
	# o processo principal cria 3 processos filhos
	SyscallFork(Programa1, 1)
	SyscallFork(Programa2, 2)
	SyscallFork(Idle, 0)

	# encerrando o processo principal
	SyscallProcessTerminate

# loop infinito com instrucao nula
Idle:
	loop:
		NOP
		j loop

# loop executa 10 vezes e encerra
Programa1:
		addi $s1, $zero, 1 # valor inicial do contador
		addi $s2, $zero, 10 # valor limite do contador
	loop1: 	addi $s1, $s1, 1
		beq $s1, $s2, fim1
		j loop1
	fim1:	SyscallProcessTerminate

# loop executa 10 vezes e encerra
Programa2:
		addi $s1, $zero, -1 # valor inicial do contador
		addi $s2, $zero, -10 # valor limite do contador
	loop2: 	addi $s1, $s1, -1
		beq $s1, $s2, fim2
		j loop2
	fim2:	SyscallProcessTerminate
