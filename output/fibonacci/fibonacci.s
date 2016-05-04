    .data
newline:	.asciiz "\n"
    .text
    .globl main
main:
    li $fp, 0x7ffffffc
    
StartBlock:
    
    # loadI 0 => r_PREV
    li $t0, 0
    sw $t0, 0($fp)
    
    # loadI 0 => r_RESULT
    li $t0, 0
    sw $t0, -4($fp)
    
    # loadI 0 => r_SUM
    li $t0, 0
    sw $t0, -8($fp)
    
    # loadI 0 => r_I
    li $t0, 0
    sw $t0, -12($fp)
    
    # loadI 0 => r_N
    li $t0, 0
    sw $t0, -16($fp)
    
    # loadI 0 => r0
    li $t0, 0
    sw $t0, -20($fp)
    
    # loadI 1 => r1
    li $t0, 1
    sw $t0, -24($fp)
    
    # subtract
    lw $t1, -20($fp)
    lw $t2, -24($fp)
    sub $t0, $t1, $t2
    sw $t0, -28($fp)
    
    # assignment => r_PREV
    lw $t0, -28($fp)
    sw $t0, 0($fp)
    
    # loadI 1 => r2
    li $t0, 1
    sw $t0, -32($fp)
    
    # assignment => r_RESULT
    lw $t0, -32($fp)
    sw $t0, -4($fp)
    
    # readInt => r_N
    li $v0, 5
    syscall
    move $t0, $v0
    sw $t0, -16($fp)
    
    # loadI 0 => r3
    li $t0, 0
    sw $t0, -36($fp)
    
    # GT
    lw $t1, -16($fp)
    lw $t2, -36($fp)
    sgt $t0, $t1, $t2
    sw $t0, -40($fp)
    
    # IfStatement
    lw $t0, -40($fp)
    beqz $t0, Else0
    
    # loadI 0 => r4
    li $t0, 0
    sw $t0, -44($fp)
    
    # assignment => r_I
    lw $t0, -44($fp)
    sw $t0, -12($fp)
    
WhileStart0:
    
    # LE
    lw $t1, -12($fp)
    lw $t2, -16($fp)
    sle $t0, $t1, $t2
    sw $t0, -48($fp)
    
    # WhileStatement
    lw $t0, -48($fp)
    beqz $t0, WhileEnd0
    
    # add
    lw $t1, -4($fp)
    lw $t2, 0($fp)
    add $t0, $t1, $t2
    sw $t0, -52($fp)
    
    # assignment => r_SUM
    lw $t0, -52($fp)
    sw $t0, -8($fp)
    
    # assignment => r_PREV
    lw $t0, -4($fp)
    sw $t0, 0($fp)
    
    # assignment => r_RESULT
    lw $t0, -8($fp)
    sw $t0, -4($fp)
    
    # loadI 0 => r5
    li $t0, 0
    sw $t0, -56($fp)
    
    # NE
    lw $t1, -4($fp)
    lw $t2, -56($fp)
    sne $t0, $t1, $t2
    sw $t0, -60($fp)
    
    # IfStatement
    lw $t0, -60($fp)
    beqz $t0, EndIf1
    
    # writeInt
    li $v0, 1
    lw $t1, -4($fp)
    move $a0, $t1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
    
EndIf1:
    
    # loadI 1 => r6
    li $t0, 1
    sw $t0, -64($fp)
    
    # add
    lw $t1, -12($fp)
    lw $t2, -64($fp)
    add $t0, $t1, $t2
    sw $t0, -68($fp)
    
    # assignment => r_I
    lw $t0, -68($fp)
    sw $t0, -12($fp)
    
    j WhileStart0
    
WhileEnd0:
    
    j EndIf0
    
Else0:
    
    # loadI 0 => r7
    li $t0, 0
    sw $t0, -72($fp)
    
    # loadI 1 => r8
    li $t0, 1
    sw $t0, -76($fp)
    
    # subtract
    lw $t1, -72($fp)
    lw $t2, -76($fp)
    sub $t0, $t1, $t2
    sw $t0, -80($fp)
    
    # writeInt
    li $v0, 1
    lw $t1, -80($fp)
    move $a0, $t1
    syscall
    li $v0, 4
    la $a0, newline
    syscall
    
EndIf0:
    
    # exit
    li $v0, 10
    syscall
