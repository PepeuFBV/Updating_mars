.macro PrintInt
	li $v0,1
	syscall
.end_macro

.macro PrintString
	li $v0,4
	syscall
.end_macro

.macro ReadInt
	li $v0,5
	syscall
.end_macro

.macro ReadString
	li $v0,8
	syscall
.end_macro

.macro Done
	li $v0,10
	syscall
.end_macro

.macro PrintChar
	li $v0,11
	syscall
.end_macro

.macro ReadChar
	li $v0,12
	syscall
.end_macro

.macro Return (%termination_value)
	li $a0, %termination_value
	bltz $a0, exit
	Done 
	exit: Exit2($a0)
.end_macro

.macro Exit2(%exit_value)
	move $a0, %exit_value
	PrintInt
	li $v0,10
	syscall
.end_macro

.macro LoadVar(%label, %reg)
	la  $t0, %label        # carrega o endereco do label em $t0
    lw  %reg, 0($t0)	# carrega o valor do label no registrador
.end_macro

.macro StoreVar(%label, %reg)
	la  $t0, %label        # carrega o endereco do label em $t0
    sw  %reg, 0($t0)	# armazena o valor no registrador
.end_macro

.macro SyscallFork(%label)
	la $a0, %label
	li $v0, 18
	syscall
.end_macro

.macro SyscallFork(%label, %priority)
	la $a0, %label
	la $a1, %priority
	li $v0, 18
	syscall
.end_macro

.macro SyscallProcessChange
	li $v0, 19
	syscall
.end_macro

.macro SyscallProcessTerminate
	li $v0, 20
	syscall
.end_macro

.macro SyscallCreateSemaphore(%init)
	la $a0, %init
	li $v0, 21
	syscall
.end_macro

.macro SyscallTerminateSemaphore(%address)
	la $a0, %address
	li $v0, 22
	syscall
.end_macro

.macro SyscallDownSemaphore(%address)
	la $a0, %address
	li $v0, 23
	syscall
.end_macro

.macro SyscallUpSemaphore(%address)
	la $a0, %address
	li $v0, 24
	syscall
.end_macro
