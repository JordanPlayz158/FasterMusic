pipeline {
  agent any
  stages {
    stage('Maven Build') {
      steps {
        sh 'mvn clean package'
      }
    }

    stage('Save Artifacts') {
      steps {
        archiveArtifacts 'target/*.jar'
      }
    }

  }
}