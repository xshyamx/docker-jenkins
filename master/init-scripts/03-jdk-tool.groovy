dis = new hudson.model.JDK.DescriptorImpl();
dis.setInstallations( new hudson.model.JDK("JDK8", "/usr/lib/jvm/java-1.8-openjdk"));
dis.save()
