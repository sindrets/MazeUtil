package no.stide;

import no.stide.generators.MazeGenerator;
import no.stide.generators.RandomizedPrim;
import no.stide.solvers.MazeSolver;
import no.stide.solvers.RecursiveSolve;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@CommandLine.Command(name = MazeUtil.commandName, versionProvider = ManifestVersionProvider.class)
public class MazeUtil {

	public static final String commandName = "mazeutil";

	@Option(names = {"-h", "--help"}, usageHelp = true, description = "Display this help message.")
	static boolean helpRequested;

	@Option(names = {"-v", "--version"}, versionHelp = true, description = "Display version info.")
	static boolean versionRequested;

	@ArgGroup(exclusive = false)
	static Config config;
	static class Config {

		@Option(names = {"-s", "--solve"}, arity = "0..1", fallbackValue = "recursive", paramLabel = "algorithm", description = "Solve the maze.")
		String solve;

		@ArgGroup
		Operation operation;
		static class Operation {

			@ArgGroup(exclusive = false)
			GenConfig genConfig;
			static class GenConfig {
				@Option(names = {"-g", "--generate"}, arity = "0..1", fallbackValue = "rprim", paramLabel = "algorithm", description = "Generate a maze.", required = true)
				String gen;
				@Option(names = {"-o", "--out"}, paramLabel = "<path>", description = "Output maze to file.")
				String out;
				@Parameters(index = "0", defaultValue = "41", description = "Maze width in number of cells.")
				int mazeWidth;
				@Parameters(index = "1", defaultValue = "41", description = "Maze height in number of cells.")
				int mazeHeight;
			}

			@ArgGroup(exclusive = false)
			FileConfig fileConfig;
			static class FileConfig {
				@Option(names = {"-f", "--file"}, paramLabel = "<path>", description = "Parse maze from file.")
				String file;
				@Option(names= {"--pattern-wall"}, defaultValue = "██", description = "The wall pattern.")
				String mazeWall;
				@Option(names= {"--pattern-path"}, defaultValue = "  ", description = "The path pattern.")
				String mazePath;
			}

		}

	}

	enum GenAlgorithm {

		RANDOMIZED_PRIM(RandomizedPrim.class, new String[]{"rprim", "randomized-prim"});

		final String[] names;
		final Class<? extends MazeGenerator> generatorClass;

		GenAlgorithm(Class<? extends MazeGenerator> generatorClass, String[] names) {
			this.names = names;
			this.generatorClass = generatorClass;
		}
	}

	enum SolveAlgorithm {

		RECURSIVE_SOLVE(RecursiveSolve.class, new String[]{"recursive", "recursive-solve"});

		final String[] names;
		final Class<? extends MazeSolver> solverClass;

		SolveAlgorithm(Class<? extends MazeSolver> solverClass, String[] names) {
			this.solverClass = solverClass;
			this.names = names;
		}
	}

	private static Maze handleGeneration() {
		if (config.operation.genConfig != null) {
			Config.Operation.GenConfig genConfig = config.operation.genConfig;

			MazeGenerator generator = null;
			if (genConfig.gen == null) {
				generator = new RandomizedPrim();
			}
			else {
				for (GenAlgorithm a : GenAlgorithm.values()) {
					if (Arrays.asList(a.names).contains(genConfig.gen)) {
						try {
							generator = a.generatorClass.getConstructor().newInstance();
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}
			if (generator == null) {
				System.err.println("Unrecognized generator algorithm: " + genConfig.gen);
				return null;
			}
			else {
				Maze maze = new Maze(genConfig.mazeWidth, genConfig.mazeHeight);
				maze.generate(generator);
				return maze;
			}
		}
		return null;
	}

	private static boolean handleSolve(Maze maze) {
		if (config.solve != null) {
			MazeSolver solver = null;
			for (SolveAlgorithm a : SolveAlgorithm.values()) {
				if (Arrays.asList(a.names).contains(config.solve)) {
					try {
						solver = a.solverClass.getConstructor().newInstance();
					} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			if (solver == null) {
				System.err.println("Unrecognized solver algorithm: " + config.solve);
				return false;
			}
			else {
				maze.solve(solver);
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {

		try {
			CommandLine cl = new CommandLine(new MazeUtil());
			cl.parseArgs(args);
			if (versionRequested) {
				cl.printVersionHelp(System.out);
				System.exit(0);
			}
			if (helpRequested || config == null) {
				cl.usage(System.out);
				System.exit(0);
			}
			if (config.operation != null) {
				if (config.operation.genConfig != null) {
					Maze maze = handleGeneration();
					if (maze != null) {
						handleSolve(maze);
						System.out.println(maze.toString());

						if (config.operation.genConfig.out != null) {
							String targetPath = new File(config.operation.genConfig.out).getCanonicalPath();
							try (PrintWriter pw = new PrintWriter(targetPath)) {
								String metadata = "";
								if (maze.getEntrance() != null) {
									metadata += "enter: " + maze.getEntrance()[0] + "," + maze.getEntrance()[1] + "\n";
								}
								if (maze.getExit() != null) {
									metadata += "exit: " + maze.getExit()[0] + "," + maze.getExit()[1] + "\n\n";
								}
								pw.println(metadata + maze.toString());
								System.out.println("Maze written to '" + targetPath + "'");
							} catch (FileNotFoundException e) {
								System.err.println("Could not write to file.");
								System.err.println(e.getMessage());
							}
						}
					}

				}
				if (config.operation.fileConfig != null) {
					Config.Operation.FileConfig fileConfig = config.operation.fileConfig;
					String targetPath = new File(fileConfig.file).getCanonicalPath();
					MazeParser mazeParser = new MazeParser(targetPath, fileConfig.mazeWall, fileConfig.mazePath);
					Maze maze = mazeParser.parse();
					handleSolve(maze);
					System.out.println(maze.toString());
				}
			}
		} catch (CommandLine.ParameterException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
