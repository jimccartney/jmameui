
mkdir bin lib
wget -O swt.zip http://www.mirrorservice.org/sites/download.eclipse.org/eclipseMirror/eclipse/downloads/drops4/R-4.3-201306052000/swt-4.3-gtk-linux-x86.zip
unzip swt.zip -d lib
make
cp -r src/jmameui/gui/icons bin/jmameui/gui