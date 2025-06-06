dependencies {
    implementation("net.pearx.kasechange:kasechange:${findProperty("kaseChangeVersion")}")//1.4.1
    implementation("it.unimi.dsi:fastutil:${findProperty("fastUtilVersion")}")//8.5.13

    testImplementation("io.mockk:mockk:${findProperty("mockkVersion")}")
}