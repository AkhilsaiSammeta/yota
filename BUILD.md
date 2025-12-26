# Build Instructions

## Development Setup

### Prerequisites
- Java JDK 8 or higher
- Any Java IDE (Eclipse recommended)
- Git (for version control)

### Quick Setup
1. Clone the repository
```bash
git clone https://github.com/AkhilsaiSammeta/yota.git
cd yota
```

2. Compile the project
```bash
# Create bin directory
mkdir bin

# Compile all Java files
javac -d bin src/**/*.java
```

3. Run the application
```bash
# Console interface
java -cp bin Main

# Web interface
java -cp bin YotaWebServer
# Open browser to http://localhost:8080
```

## Build Scripts

### Console Application
```bash
# Compile
javac -cp bin -d bin src/core/*.java src/evaluation/*.java src/ui/*.java src/io/*.java src/algorithms/core/*.java src/algorithms/classifier/*.java src/Main.java

# Run
java -cp bin Main
```

### Web Application
```bash
# Compile
javac -cp bin -d bin src/YotaWebServer.java

# Run
java -cp bin YotaWebServer
```

## Creating Distribution Package

Use the provided build scripts:

```bash
# Simple portable package
build-portable-simple.bat

# Complete installer package  
build-ultimate.bat
```

## IDE Setup

### Eclipse
1. Import as existing project
2. Set build path to include all source folders
3. Run configurations:
   - Main class: `Main` (console)
   - Main class: `YotaWebServer` (web)

### IntelliJ IDEA
1. Open project folder
2. Mark `src` as source root
3. Configure run configurations as above

### VS Code
1. Install Java extensions
2. Open project folder
3. Use integrated terminal for compilation/running

## Testing

Run with sample data:
```bash
java -cp bin Main
# Uses sample_data.csv automatically

java -cp bin YotaWebServer  
# Load sample data via web interface
```

## Troubleshooting

### Common Issues
- **Java not found**: Ensure Java is in PATH
- **Compilation errors**: Check all dependencies are compiled
- **Port conflicts**: Web server auto-detects available ports
- **Permission issues**: Run as administrator if needed

### Build Verification
```bash
# Verify classes compiled correctly
ls bin/
# Should show compiled .class files

# Test basic functionality
java -cp bin Main --version
```