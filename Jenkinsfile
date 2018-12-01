pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo "Compiling..."
                sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt compile"
                echo "Compiled"
            }
        }
        stage('Download required files') {
            steps {
                script {
                    response = sh(
                            returnStdout: true,
                            script: "sudo apt-get install wget -y"
                    )
                }
                script {
                    response = sh(
                            returnStdout: true,
                            script: "wget -O ./data/ud-treebanks-v2.2/en_ewt-ud-train.conllu https://github.com/UniversalDependencies/UD_English-EWT/raw/master/en_ewt-ud-train.conllu"
                    )
                }
                script {
                    response = sh(
                            returnStdout: true,
                            script: "wget -O ./data/ud-treebanks-v2.2/en_ewt-ud-dev.conllu https://github.com/UniversalDependencies/UD_English-EWT/raw/master/en_ewt-ud-dev.conllu"
                    )

                }
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt test"
                echo 'Testing finished'
            }
        }
        stage('Assembly') {
            steps {
                echo 'Running assembly...'
            }
        }
    }
}