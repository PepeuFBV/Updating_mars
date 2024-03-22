.include "macros.asm"

.globl main
.data
	mutex: .word 1
	empty: .word 5
	full: .word 0

.text
main:
	SyscallCreateSemaphore(mutex)
	SyscallCreateSemaphore(empty)
	SyscallCreateSemaphore(full)
	
	SyscallFork(produtor)
	SyscallFork(consumidor)
	
	
produtor:
	# Verificar se o buffer está cheio
	# Se estiver cheio, bloqueia
	# Caso contrário, consome o buffer

consumidor:
	