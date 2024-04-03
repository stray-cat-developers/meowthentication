package org.straycats.meowthentication.api.common

enum class ErrorCode(val summary: String) {

    H000("Human error"),

    HD00("Data not found"),
    HD01("No results found"),
    HD02("Data mismatch uri"),

    HA00("Unauthorized"),
    HA01("Unauthorized Data Relation"),
    HA02("Password is incorrect"),
    HA03("access denied for user"),

    HI00("Invalid Input"),
    HI01("Invalid argument"),
    HI02("Invalid header argument"),

    P000("policy error"),
    P002("password rule"),
    PD01("develop mistake error"),
    PD02("Audit Fail. This feature is only available when you can log in. Please check the policy."),
    PL01("duplicate request"),
    PL02("Contact the Supervisor"),

    S000("common system error"),
    SA00("async execute error"),
    SI01("illegal state error"),
    SD01("database access error"),

    C000("communication error"),
    CT01("connection timeout"),
    CT02("read timeout"),
    CT03("response error"),

    FS00("front sync error"),
    FS01("change password"),
    FS02("must sign in"),

    ;

    override fun toString(): String {
        return "${this.name}::${this.summary}"
    }
}
