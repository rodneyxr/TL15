    .data
newline:	.asciiz "\n"
    .text
    .globl main
main:
    li $fp, 0x7ffffffc
    
StartBlock:
    
    # loadI 0 => r_N
    li $t0, 0
    sw $t0, 0($fp)
    
    # loadI 0 => r_SQRT
    li $t0, 0
    sw $t0, -4($fp)
    
    # loadI 0 => r_SQRT2
    li $t0, 0
    sw $t0, -8($fp)
    
    # readInt => r_N
    li $v0, 5
    syscall
    move $t0, $v0
    sw $t0, 0($fp)
    
    # loadI 0 => r0
    li $t0, 0
    sw $t0, -12($fp)
    
    # assignment => r_SQRT
    lw $t0, -12($fp)
    sw $t0, -4($fp)
    
    # loadI 1 => r1
    li $t0, 1
    sw $t0, -16($fp)
    
    # assignment => r_SQRT2
    lw $t0, -16($fp)
    sw $t0, -8($fp)
    
WhileStart0:
    
    # multiply
    lw $t1, -4($fp)
    lw $t2, -4($fp)
    mul $t0, $t1, $t2
    sw $t0, -20($fp)
    
    # LE
    lw $t1, -20($fp)
    lw $t2, 0($fp)
    sle $t0, $t1, $t2
    sw $t0, -24($fp)
    
    # WhileStatement
    lw $t0, -24($fp)
    beqz $t0, WhileEnd0
    
    # loadI 1 => r2
    li $t0, 1
    sw $t0, -28($fp)
    
    # add
    lw $t1, -4($fp)
    lw $t2, -28($fp)
    add $t0, $t1, $t2
    sw $t0, -32($fp)
    
    # assignment => r_SQRT
    lw $t0, -32($fp)
    sw $t0, -4($fp)
    
    # assignment => r_SQRT2
    lw $t0, -4($fp)
    sw $t0, -8($fp)
    
    j WhileStart0
    
WhileEnd0:
    
    # loadI 1 => r3
    li $t0, 1
    sw $t0, -36($fp)
    
    # subtract
    lw $t1, -4($fp)
    lw $t2, -36($fp)
    sub $t0, $t1, $t2
    sw $t0, -40($fp)
    
    # assignment => r_SQRT
    lw $t0, -40($fp)
    sw $t0, -4($fp)
    
    # writeInt
    li $v0, 1
    lw $t1, -4($fp)
    move $a0, $t1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
    
    # exit
    li $v0, 10
    syscall
