// !CHECK_TYPE
// !DIAGNOSTICS: -UNUSED_PARAMETER
data class A(val x: Int, val y: String)
data class B(val u: Double, val w: Short)

fun foo(block: (A, B) -> Unit) { }

fun bar() {
    foo { (<!REDECLARATION!>a<!>, <!REDECLARATION!>a<!>), b ->
        a checkType { <!TYPE_MISMATCH!>_<!><Int>() }
        b checkType { <!TYPE_MISMATCH!>_<!><String>() }
    }

    foo { (<!REDECLARATION!>a<!>, b), <!REDECLARATION!>a<!> ->
        a checkType { <!TYPE_MISMATCH!>_<!><Int>() }
        b checkType { _<String>() }
    }

    foo { <!REDECLARATION!>a<!>, (<!REDECLARATION!>a<!>, b) ->
        a checkType { <!TYPE_MISMATCH!>_<!><Int>() }
        b checkType { <!TYPE_MISMATCH!>_<!><String>() }
    }

    foo { (a, <!REDECLARATION!>b<!>), (c, <!REDECLARATION!>b<!>) ->
        a checkType { _<Int>() }
        b checkType { <!TYPE_MISMATCH!>_<!><String>() }
        c checkType { <!TYPE_MISMATCH!>_<!><B>() }
    }
}
