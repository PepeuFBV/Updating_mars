# Mars Software Modification for MIPS Architecture - Updating Mars

## Project Description

This project is a modification of the Mars software for the MIPS architecture. The implementation corresponds to part of the operating system infrastructure to perform process management, such as creation of Syscalls, PCB, and process scheduling.

## Getting Started

These instructions will provide a copy of the project running on your local machine for development and testing purposes.

### Prerequisites

What you need to install the software:

- Java Runtime Environment (JRE) 8 or higher
- Mars MIPS Simulator

### Installation

1. Clone the repository
2. Navigate to the project folder
3. Compile the project

## Project Details

The project was developed under the Operating Systems course at the Federal University of the Semi-Arid Rural Area (UFERSA). The project was developed by the following students:
- Afonso Simão
- Breno Klywer
- Pedro Figueira

## Implementation Steps

The project was divided into the following parts:

- Part 1
    - Step 1: PCB Creation, Process Table and Process Scheduling
    - Step 2: Syscalls Implementation for Process Management
    - Step 3.1: Preemptive Scheduling
    - Step 3.2: New Scheduling Algorithms
    - Step 4: Process Synchronization through Semaphores
- Part 2
    - Step 1: Virtual Memory Management Structures
    - Step 2: MMU and Page Table Implementation

- Obs: The following macros were used in the assembly code throughout the project:

_macros.asm_
```assembly
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
	lw %reg, %label
.end_macro

.macro StoreVar(%label, %reg)
	sw %reg, %label
.end_macro

.macro SyscallFork(%label)
	la $a0, %label
	li $a2, 0		# differentiates the SyscallFork macros to a unique Java class
	li $v0, 18
	syscall
.end_macro

.macro SyscallFork(%label, %priority)
	la $a0, %label
	la $a1, %priority
	li $a2, 1		# if $a2 value is 1, theres a process with priority
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
	lw $a1, %init
	li $v0, 21
	syscall
.end_macro
```
---
### Part 1

### Step 1: PCB Creation, Process Table and Process Scheduling

#### Process Control Block (PCB) Structure and Implementation

The first step of the project was to create the PCB structure and implement the process scheduling. The PCB structure was created with the following fields:

- A global pointer register
- An stack pointer register
- All general-purpose registers
- Program counter register
- Current process state
- Process ID
- Program address

The Process Control Block (PCB) Structure serves to store the process information, such as the process state, process ID, and the registers' values. It also has 2 important methods:

- `copyFromHardware()` - copies the values of the registers from the hardware to the PCB class
- `copyToHardware()` - copies the values of the registers from the PCB class to the hardware

#### Process Table Implementation

The process table was implemented as a Deque, for the ready processes (`Deque<ProcessControlBlock>`), and a variable for the currently in execution process. The process table has the methods for adding a process to the ready queue, removing a process from the ready queue and changing the state of a process. It also has a static counter for the process ID for newly created processes.

#### Process Scheduling Implementation

The process scheduling was implemented using a FIFO queue. The scheduling algorithm is simple: the process that is at the front of the queue is the one that will be executed. The scheduling algorithm only has the `shedule()` method, which is responsible for changing the next process state to `RUNNING` and copying the process information to the hardware.

---
### Step 2: Syscalls Implementation for Process Management

#### Syscalls Implementation

The Syscalls were implemented to allow the user to create a new process, change the process in execution and finish a process. So, the Syscalls implemented were:

- `SyscallFork`: Creates a new process by copying the current process information to a new PCB. The new process is added to the process table and the process ID is incremented. The parameters are the syscall number and the initial address of the new process.
- `SyscallProcessChange`: Changes the process in execution. The current process is added to the ready queue in the process table and the next process is removed from the ready queue and its state is changed to `RUNNING`. There is only one parameter, the syscall number.
- `SyscallProcessTerminate`: Terminates the process in execution. The current process is finished, its removed from the process table and the next process starts to execute. There is only one parameter, the syscall number.

Extra - For testing purposes the following assembly code was used:

```assembly
.include "macros.asm"

.data
.text             
	#criação dos processos
SyscallFork(Programa1)
	SyscallFork(Programa2)
	SyscallFork(Idle)
	#escalonando o primeiro processo
SyscallProcessChange
	
Idle:					
	loop:
SyscallProcessChange
		j loop
```

```assembly
Programa1:					
		addi $s1, $zero, 1 # valor inicial do contador
		addi $s2, $zero, 10 # valor limite do contador
	loop1: 	addi $s1, $s1, 1
		beq $s1, $s2, fim1
SyscallProcessChange
		j loop1
	fim1:	SyscallProcessTerminate

Programa2: 
		addi $s1, $zero, -1 # valor inicial do contador
		addi $s2, $zero, -10 # valor limite do contador
	loop2: 	addi $s1, $s1, -1
		beq $s1, $s2, fim2
SyscallProcessChange
		j loop2
	fim2:	SyscallProcessTerminate

```

---
### Step 3.1: Preemptive Scheduling

For the preemptive scheduling, the project was modified by the creation of a timer tool. The timer tool was implemented to interrupt the process in execution after a certain time. The user is able to set the time for the timer tool through inputing the number of instructions that the process in execution will execute before being interrupted, there are also 3 more options: Connect to MIPS, reset, help and exit. When the process is interrupted, the timer is reset and starts counting again.

The interruption switches the process in execution to the ready queue and the next process starts to execute. This mechanism is a substitute for `SyscallProcessChange` and is used to implement the preemptive scheduling.

Extra - For testing purposes the following assembly code was used:

```assembly	
.include "macros.asm"

.data
.text             
	#criação dos processos
SyscallFork(Programa1)
	SyscallFork(Programa2)
	SyscallFork(Idle)
	#escalonando o primeiro processo
SyscallProcessChange
	
Idle:					
	loop:
NOP
j loop
```

```assembly
Programa1:					
		addi $s1, $zero, 1 # valor inicial do contador
		addi $s2, $zero, 10 # valor limite do contador
	loop1: 	addi $s1, $s1, 1
		beq $s1, $s2, fim1
j loop1
	fim1:	SyscallProcessTerminate

Programa2: 
		addi $s1, $zero, -1 # valor inicial do contador
		addi $s2, $zero, -10 # valor limite do contador
	loop2: 	addi $s1, $s1, -1
		beq $s1, $s2, fim2
		j loop2
	fim2:	SyscallProcessTerminate
```

---
### Step 3.2: New Scheduling Algorithms

For the new scheduling algorithms, the project was modified by the creation of two new scheduling algorithms: Fixed Priority and Lottery. The Fixed Priority algorithm is a preemptive scheduling algorithm that assigns a priority to each process and the process with the highest priority is the one that will be executed. The Lottery algorithm is a non-preemptive scheduling algorithm that assigns a number of tickets to each process and the process that wins the lottery is the one that will be executed (in our implementation, the process is chosen randomly).

In the Timer Tool, the user is able to choose the scheduling algorithm that will be used.

Extra - For testing purposes the following assembly code was used:

```assembly
.include "macros.asm"

.data
.text             
      #criação dos processos com prioridade
SyscallFork(Programa1, 1)
	SyscallFork(Programa2, 2)
	SyscallFork(Idle, 0)
	#escalonando o primeiro processo
SyscallProcessChange
	
Idle:					
	loop:
NOP
j loop
```

```assembly
Programa1:					
		addi $s1, $zero, 1 # valor inicial do contador
		addi $s2, $zero, 10 # valor limite do contador
	loop1: 	addi $s1, $s1, 1
		beq $s1, $s2, fim1
j loop1
	fim1:	SyscallProcessTerminate

Programa2: 
		addi $s1, $zero, -1 # valor inicial do contador
		addi $s2, $zero, -10 # valor limite do contador
	loop2: 	addi $s1, $s1, -1
		beq $s1, $s2, fim2
		j loop2
	fim2:	SyscallProcessTerminate
```

---
### Step 4: Process Synchronization through Semaphores

For synchronization of the processes, we implemented 4 extra syscalls for the semaphores: `CreateSemaphore`, `TerminateSemaphore`, `DownSemaphore` and `UpSemaphore`.

- `CreateSemaphore`: Creates a list in which the processes that are waiting for the semaphore are stored as blocked. The syscall receives an integer as a parameter, which is the semaphore address, used as the initial address of the semaphore.
- `TerminateSemaphore`: Terminates the semaphore, removing the semaphore from the list of semaphores. Through the syscall, the semaphore address is passed as a parameter, and the list associated with the semaphore is removed.
- `DownSemaphore`: Decreases the semaphore value. If after the syscall the semaphore value is less than 0 or equal to 0, the process is blocked. The syscall receives the semaphore address as a parameter.
- `UpSemaphore`: Increases the semaphore value. If there is a process blocked in the semaphore list, the process is unblocked. The syscall receives the semaphore address as a parameter.

Extra - For testing purposes we created the producer-consumer problem using semaphores. The following assembly code was used:

_semaphore.asm_
```assembly
.include "macros.asm"

.data
	mutex: .word 1
	empty: .word 0
	full: .word 10
	buffer: .word 1, 2, 3, 4, 5, 6, 7, 8, 9, 10

.text
main:
	# Cria os semáforos
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
		SyscallDownSemaphore(mutex)    # Entra na região crítica (se mutex > 0)

		addi $s0, $s0, 1    # Produz um item (produz valores de 1 a 10)
		lw $t1, full		# Carrega o valor de full
		mul $t1, $t1, 4		# Multiplica full por 4 bytes = quantidade de inteiros no buffer
		add $t2, $t1, $t0	# Salva o deslocamento do endereço do buffer em $t2 (topo)
		sw $s0, 0($t2)      # Armazena o valor do registrador $s0 no topo do buffer

		SyscallUpSemaphore(mutex)    # Libera acesso ao buffer
		SyscallUpSemaphore(full)     # Incrementa o contador de itens no buffer
		j loop1
	fim1:	SyscallProcessTerminate

consumidor:
	la $t0, buffer      # Carrega o endereco do buffer
	loop2:
		SyscallDownSemaphore(full)    # Verifica se pode consumir
		SyscallDownSemaphore(mutex)    # Entra na região crítica (se mutex > 0)

		lw $t1, full		# Carrega o valor de full
		mul $t1, $t1, 4		# Multiplica full por 4 bytes = quantidade de inteiros no buffer
		add $t2, $t1, $t0	# Salva o deslocamento do endereço do buffer em $t2 (topo)
		sw $0, 0($t2)       # Consome um item do buffer salvando o valor 0

		SyscallUpSemaphore(mutex)    # Libera acesso ao buffer
		SyscallUpSemaphore(empty)    # Incrementa o contador de espaços vazios no buffer
		j loop2	
	fim2:	SyscallProcessTerminate
```

---
### Part 2

### Step 1: Virtual Memory Management Structures

For the virtual memory management structures, we implemented registers for the upper and lower limits of the process memory in the PCB. The registers are used to store the memory limits of the process and every time the process is changed, the memory limits are updated. The memory limits are used to check if the process is trying to access a memory address that is out of its memory limits. A memory manager class was created to manage the memory limits and check if the process is trying to access a memory address that is out of its memory limits.

Extra - For testing purposes the following assembly code was used:

```assembly
.include "macros.asm"

.data
.text             
	#criação dos processos
SyscallFork(Programa1)
	SyscallFork(Programa2)
	SyscallFork(Idle)
	#escalonando o primeiro processo
SyscallProcessChange
	
Idle:					
	loop:
NOP
j loop
```

```assembly
Programa1:					
		addi $s1, $zero, 1 # valor inicial do contador
		addi $s2, $zero, 10 # valor limite do contador
	loop1: 	addi $s1, $s1, 1
		beq $s1, $s2, fim1
j loop1
	fim1:	SyscallProcessTerminate

Programa2: 
		addi $s1, $zero, -1 # valor inicial do contador
		addi $s2, $zero, -10 # valor limite do contador
	loop2: 	addi $s1, $s1, -1
		beq $s1, $s2, fim2
		j loop2
	fim2:	SyscallProcessTerminate
```

---
### Step 2: MMU and Page Table Implementation

For this part of the project, we implemented the MMU, VirtualTable and VirtualTableEntry classes. The MMU class is responsible for translating the virtual address to the physical address and maintaining the page table. The VirtualTable class is responsible for storing the page table entries and the VirtualTableEntry class is responsible for storing the page table entry information. A tool was also created to auxiliate the user, the Memory Management Tool, which allows the user to set the page size, the number of pages, choose the paging method, see all the page table entries, see the physical address of a virtual address, see the frames, see the total number of page faults, hits and misses, and reset the page table.

The paging methods that were implemented: FIFO, LRU, NRU and Second Chance, through the tool the user is able to choose the paging method that will be used and also set the page size and the number of pages. Allowing for the quick and easy management of the page table entries.

![Memory Management Tool](/MMUTool.jpg)