pipeline {
  agent any
  stages {
    stage('Release') {
      steps {
        sh 'mvn validate'
        sh 'mvn compile'
        sh 'mvn package'
      }
    }
    stage('Artifact') {
      steps {
        archiveArtifacts(artifacts: 'DBTool.war', onlyIfSuccessful: true)
      }
    }
    stage('Complete') {
      steps {
        echo 'DONE'
      }
    }
  }
}